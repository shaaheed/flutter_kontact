
import 'dart:async';

import 'package:flutter/services.dart';

class ContactService {
  static const MethodChannel _channel =
      const MethodChannel('com.voidlab.flutter_kontact/contact');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> open([Map map]) async {
    await _channel.invokeMethod('open', map);
  }

}
