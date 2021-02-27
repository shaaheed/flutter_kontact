#import "FlutterKontactPlugin.h"
#if __has_include(<flutter_kontact/flutter_kontact-Swift.h>)
#import <flutter_kontact/flutter_kontact-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_kontact-Swift.h"
#endif

@implementation FlutterKontactPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterKontactPlugin registerWithRegistrar:registrar];
}
@end
