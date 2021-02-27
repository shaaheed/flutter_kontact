package com.voidlab.flutter_kontact

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.ContactsContract
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class ContactService(private val binding: FlutterPlugin.FlutterPluginBinding): MethodCallHandler, ActivityResultListener  {

    private val REQUEST_OPEN_CONTACT_FORM = 52941
    private val REQUEST_OPEN_EXISTING_CONTACT = 52942
    private val REQUEST_OPEN_CONTACT_PICKER = 52943
    private val FORM_OPERATION_CANCELED = 1
    private val FORM_COULD_NOT_BE_OPEN = 2

    private var result: Result? = null
    private var activityPluginBinding: ActivityPluginBinding? = null
    private var context: Context = binding.applicationContext
    private var channel : MethodChannel
//    private val context: Context? = null

    init {
        channel = MethodChannel(binding.binaryMessenger, "com.voidlab.flutter_kontact/contact")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when(call.method) {
            "getPlatformVersion" -> result.success("Android ${Build.VERSION.RELEASE}")
            "open" -> {
                if (call.arguments != null) {
                    open(call.arguments as Map<String, String>)
                }
                else {
                    open(null)
                }
            }
            else -> result.notImplemented()
        }
    }

    fun setResult(result: Result?) {
        this.result = result
    }

    fun bind(activityPluginBinding: ActivityPluginBinding?) {
        this.activityPluginBinding = activityPluginBinding
        this.activityPluginBinding!!.addActivityResultListener(this)
    }

    fun unbind() {
        activityPluginBinding!!.removeActivityResultListener(this)
        activityPluginBinding = null
    }

    fun finishWithResult(result: Any?) {
        if (this.result != null) {
            this.result!!.success(result)
            this.result = null
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    fun open(args: Map<String, String>?) {
        try {
            val intent = Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI)
            intent.putExtra("finishActivityOnSaveCompleted", true)

            if (args != null) {
                if (args["phone"] != null) {
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, args["phone"])
                }
            }

            startIntent(intent, REQUEST_OPEN_CONTACT_FORM)
        } catch (e: Exception) {
        }
    }

    fun startIntent(intent: Intent, request: Int) {
        if (this.activityPluginBinding != null) {
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                activityPluginBinding!!.getActivity().startActivityForResult(intent, request)
            } else {
                finishWithResult(FORM_COULD_NOT_BE_OPEN)
            }
        } else {
            context.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?): Boolean {
        if (requestCode == REQUEST_OPEN_EXISTING_CONTACT || requestCode == REQUEST_OPEN_CONTACT_FORM) {
            try {
                val ur = intent!!.data
                finishWithResult(ur!!.lastPathSegment)
            } catch (e: NullPointerException) {
                finishWithResult(FORM_OPERATION_CANCELED)
            }
            return true
        }
        return false
    }

    fun dispose() {
        channel.setMethodCallHandler(null)
    }

}