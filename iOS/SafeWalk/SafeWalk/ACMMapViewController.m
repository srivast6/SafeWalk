//
//  ACMMapViewController.m
//  SafeWalk
//
//  Created by Eric Templin on 2/4/14.
//  Copyright (c) 2014 Eric Templin. All rights reserved.
//

#import "ACMMapViewController.h"

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
    [self setMapView:gmsMapView];
    //[gmsMapView setNeedsDisplay];
    //self.navigationController.toolbarHidden = NO;
    [self.navigationController setToolbarHidden:NO];
    NSLog(@"%d toolbarItems", self.navigationController.toolbarItems.count);
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
