package com.voidlab.flutter_kontact

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

class FlutterKontactPlugin: FlutterPlugin, ActivityAware {
  private lateinit var contactService : ContactService

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    contactService = ContactService(binding)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    contactService.bind(binding)
  }

  override fun onDetachedFromActivity() {
    contactService.unbind()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    contactService.bind(binding)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    contactService.unbind()
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    contactService.dispose()
  }
}
