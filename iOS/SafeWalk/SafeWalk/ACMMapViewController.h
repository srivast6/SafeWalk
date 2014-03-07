//
//  ACMMapViewController.h
//  SafeWalk
//
//  Created by Eric Templin on 2/4/14.
//  Copyright (c) 2014 Eric Templin. All rights reserved.
//

#import <GoogleMaps/GoogleMaps.h>
#import <UIKit/UIKit.h>

@interface ACMMapViewController : UIViewController <CLLocationManagerDelegate>

@property IBOutlet UIView * mapView;

- (IBAction)callSafeWalkButtonPressed:(UIBarButtonItem*)sender;

- (IBAction)call411ButtonPressed:(UIBarButtonItem*)sender;

@end
