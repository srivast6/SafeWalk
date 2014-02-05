//
//  ACMMapViewController.m
//  SafeWalk
//
//  Created by Eric Templin on 2/4/14.
//  Copyright (c) 2014 Eric Templin. All rights reserved.
//

#import "ACMMapViewController.h"
#import "ACMAppDelegate.h"

@interface ACMMapViewController ()

@end

@implementation ACMMapViewController {
    GMSMapView * gmsMapView;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:-33.86
                                                            longitude:151.20
                                                                 zoom:6];
    gmsMapView = [GMSMapView mapWithFrame:CGRectZero camera:camera];
    gmsMapView.myLocationEnabled = YES;
    gmsMapView.settings.myLocationButton = YES;
    
    // Set map center to current location
    if([CLLocationManager locationServicesEnabled] == NO) {
        UIAlertView * locationServicesDisabledAlert = [[UIAlertView alloc] initWithTitle:@"Location Required" message:@"You must enable Location Services to use this app." delegate:nil cancelButtonTitle:@"Close" otherButtonTitles:@"Open Settings", nil];
        [locationServicesDisabledAlert show];
    }
    
    if([CLLocationManager authorizationStatus] == kCLAuthorizationStatusDenied || [CLLocationManager authorizationStatus] == kCLAuthorizationStatusRestricted) {
        UIAlertView * locationNotAuthorizedAlert = [[UIAlertView alloc] initWithTitle:@"Location Required" message:@"You must authorize SafeWalk to access your location to use the app." delegate:nil cancelButtonTitle:@"Close" otherButtonTitles:@"Open Settings", nil];
        //TODO: Set delegate to something that will open up location settings or authorize location.
        [locationNotAuthorizedAlert show];
    }
    
    ACMAppDelegate * del = [[UIApplication sharedApplication] delegate];
    del.locationManager = [CLLocationManager alloc];
    //del.locationManager.delegate = self;
    //[del.locationManager startUpdatingLocation];

    
    [self setMapView:gmsMapView];
    
    [self.navigationController setToolbarHidden:NO];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray*)locations
{
    CLLocation * loc = locations.firstObject;
    [gmsMapView animateToLocation:loc.coordinate];
}

- (IBAction)callSafeWalkButtonPressed:(UIBarButtonItem*)sender
{
    [self callPhoneNumber:@"tel:765-494-7233"];
}

- (IBAction)call911ButtonPressed:(UIBarButtonItem*)sender
{
    [self callPhoneNumber:@"tel:911"];
}

- (void)callPhoneNumber:(NSString*)phoneURL
{
    UIDevice *device = [UIDevice currentDevice];
    if([[device model] isEqualToString:@"iPhone"] ) {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@",phoneURL]]];
    } else {
        [[[UIAlertView alloc] initWithTitle:@"Error" message:@"Your device doesn't support this feature." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
