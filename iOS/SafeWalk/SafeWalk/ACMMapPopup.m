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
    CGContextRef con = UIGraphicsGetCurrentContext();
    CGContextSetRGBFillColor(con, 1.0, 0, 0, 1);
    CGContextSetRGBStrokeColor(con, 1.0, 1.0, 1.0, 1.0);
    CGContextMoveToPoint(con, CGRectGetMinX(rect), CGRectGetMinY(rect));
    CGContextAddLineToPoint(con, CGRectGetMaxX(rect), CGRectGetMinY(rect));
    CGContextAddLineToPoint(con, CGRectGetMaxX(rect), CGRectGetMaxY(rect));
    CGContextAddLineToPoint(con, CGRectGetMinX(rect), CGRectGetMaxY(rect));
    CGContextClosePath(con);
    CGContextFillPath(con);
    
}


@end
