//
//  SecondViewController.h
//  HelloWorld
//
//  Created by student on 05/12/2022.
//  Copyright © 2022 student. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class SecondViewController;

@protocol SecondViewControllerDelegate <NSObject>

-(void) addItemViewController:(SecondViewController *) controller didFinishEnteringItem: (NSString *)item;

@end

@interface SecondViewController : UIViewController


@property (nonatomic, weak) IBOutlet UITextField *modifiedNameInputField;
@property (nonatomic, weak) IBOutlet UITextField *modifiedSurnameInputField;
@property (nonatomic, weak) id <SecondViewControllerDelegate> delegate;

@property NSString *Surname;
@end

NS_ASSUME_NONNULL_END
