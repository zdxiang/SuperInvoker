# SuperInvoker
[![Developer](https://img.shields.io/badge/Developer-Jm-brightgreen.svg)](https://github.com/zdxiang)

#### Features(Not 100%)
* Keep your application live.
* Wake up your application.


#### Step 1
###### Gradle 
* compile 'com.github.zdxiang:SuperInvoker:the lasted version'

###### Maven
	<dependency>
	    <groupId>com.github.User</groupId>
	    <artifactId>Repo</artifactId>
	    <version>Tag</version>
	</dependency>
    
#### Step 2
* Make sure that Your Application extends DaemonApplication and overried the methods
* Init the invokerEngin in 'startYourService'
* Return your working processName in 'getYourProcessName'.E.g cn.zdxiang.test:process
* Return your empty processName in 'getCoreProcessName'.E.g cn.zdxiang.test:coreprocess
* Return your working service CanonicalName that extends BaseBizService in 'getYourServiceCanonicalName'.E.g  MyService.class.getCanonicalName();

#### Step 3
* Set your manifest file

You can guide the user to set the automatic start.<br>

    if (!IntentWrapperUtils.isIntentWrapperSet(context)) { 
    IntentWrapper.whiteListMatters(context, activity, "some tips you want"); 
    };

## For more details,see the samples
