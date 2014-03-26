//
//  ACMMapViewController.m
//  SafeWalk
//
//  Created by Eric Templin on 2/4/14.
//  Copyright (c) 2014 Eric Templin. All rights reserved.
//

#import "ACMMapViewController.h"
#import "ACMAppDelegate.h"

// Used to read events in the mapView
#pragma mark - GMSMapViewDelegate

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

    // Padding the map so our buttons still appear:
                                        /*    Top,  L,  Bot,  R   */
    UIEdgeInsets mapInsets = UIEdgeInsetsMake(0.0, 0.0, 50.0, 0.0);
    gmsMapView.padding = mapInsets;
    
    // Enabling myLocation and adding it to the map:
    gmsMapView.myLocationEnabled = YES;
    gmsMapView.settings.myLocationButton = YES;
    gmsMapView.delegate = self;
    self.view = gmsMapView;
    
    _marker = [[GMSMarker alloc] init];
    _marker.position = CLLocationCoordinate2DMake(-33.86, 151.20);
    _marker.title = @"Hello World";//@"Sydney";
    //_marker.snippet = @"Australia";
    _marker.appearAnimation = kGMSMarkerAnimationNone;
    _marker.map = gmsMapView;
    
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


- (void)mapView:(GMSMapView *)mapView
didTapAtCoordinate:(CLLocationCoordinate2D)coordinate {
    NSLog(@"You tapped at %f,%f", coordinate.latitude, coordinate.longitude);
}

- (void)mapView:(GMSMapView *)mapView
didChangeCameraPosition:(GMSCameraPosition *)cameraPosition {
    _marker.position = cameraPosition.target;
    _marker.map = mapView;
}

- (IBAction)callSafeWalkButtonPressed:(UIBarButtonItem*)sender
{
    [self callPhoneNumber:@"tel:765-494-7233"];
}

- (IBAction)call411ButtonPressed:(UIBarButtonItem*)sender
{
    [self callPhoneNumber:@"tel:411"];
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
