//
//  ACMAppDelegate.h
//  SafeWalk
//
//  Created by Eric Templin on 2/4/14.
//  Copyright (c) 2014 Eric Templin. All rights reserved.
//

#import <GoogleMaps/GoogleMaps.h>
#import <UIKit/UIKit.h>

@interface ACMAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property CLLocationManager *locationManager;

@end
