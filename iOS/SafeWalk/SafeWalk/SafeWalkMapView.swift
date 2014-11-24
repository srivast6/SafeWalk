//
//  SafeWalkMapView.swift
//  SafeWalk
//
//  Created by Eric Templin on 11/13/14.
//  Copyright (c) 2014 Purdue University. All rights reserved.
//

import UIKit

class SafeWalkMapView : GMSMapView {
    
    @IBOutlet weak var bubble: UIView!
    @IBOutlet weak var pin: UIImageView!
    
    let activeTouches: NSMutableSet = NSMutableSet.alloc();
    
    override func touchesBegan(touches: NSSet, withEvent event: UIEvent) {
        super.touchesBegan(touches, withEvent: event)
        NSLog("touchesBegan")
        activeTouches.addObjectsFromArray(touches.allObjects)
        changePinVisibility()
    }
    
    override func touchesEnded(touches: NSSet, withEvent event: UIEvent) {
        NSLog("touchesEnded")
        for touch in touches {
            activeTouches.removeObject(touch)
        }
        changePinVisibility()
    }
    
    override func touchesCancelled(touches: NSSet!, withEvent event: UIEvent!) {
        NSLog("touchesCancelled")
        for touch in touches {
            activeTouches.removeObject(touch)
        }
        changePinVisibility()
    }
    
    func changePinVisibility() {
        if(activeTouches.count > 0) {
            bubble.hidden = true
        } else {
            bubble.hidden = false
        }
    }
}