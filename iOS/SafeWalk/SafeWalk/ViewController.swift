//
//  ViewController.swift
//  SafeWalk
//
//  Created by Eric Templin on 11/5/14.
//  Copyright (c) 2014 Purdue University. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    @IBOutlet weak var mapView: SafeWalkMapView!

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        var camera = GMSCameraPosition.cameraWithLatitude(40.427605, longitude:-86.916962, zoom:17)
        mapView.camera = camera
        
        mapView.myLocationEnabled = true
        mapView.settings.myLocationButton = true
        mapView.settings.tiltGestures = false
        mapView.settings.rotateGestures = false        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func callSafeWalkButtonPressed(UIButton) {
        self.callPhoneNumber("tel:765-494-7233")
    }
    
    @IBAction func callPoliceButtonPressed(UIButton) {
        self.callPhoneNumber("tel:411")
    }
    
    func callPhoneNumber(phoneURL:NSString) {
        var callNumberURL = NSURL(string: phoneURL)
        if(UIApplication.sharedApplication().canOpenURL(callNumberURL!)) {
            UIApplication.sharedApplication().openURL(callNumberURL!);
        } else {
            var alertView = UIAlertView()
            alertView.title = "Error"
            alertView.message = "Your device doesn't support this feature."
            alertView.addButtonWithTitle("OK")
            alertView.show()
        }
    }

}

