//
//  ACMMapPopup.m
//  SafeWalk
//
//  Created by Eric Templin on 4/1/14.
//  Copyright (c) 2014 Eric Templin. All rights reserved.
//

#import "ACMMapPopup.h"

@implementation ACMMapPopup

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        [self setItemPropertiesToDefault:self];
    }
    return self;
}

- (void)setItemPropertiesToDefault:sender
{
    //[self setLocation:NSMakePoint(0.0,0.0)];
    //[self setItemColor:[NSColor redColor]];
    //[self setBackgroundColor:[NSColor whiteColor]];
}

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
    
}


@end
