# SuperInvoker
[![Developer](https://img.shields.io/badge/Developer-Jm-brightgreen.svg)](https://github.com/zdxiang)

#### Features(Not 100%,Please use carefully)
* Keep your application live.
* Wake up your application.


#### Step 1
###### Gradle
* compile 'com.github.zdxiang:SuperInvoker:the lasted version'

###### Maven
		<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	
	
		<dependency>
	    <groupId>com.github.zdxiang</groupId>
	    <artifactId>SuperInvoker</artifactId>
	    <version>the lasted version</version>
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

## oom_adj
![alt text](https://github.com/zdxiang/SuperInvoker/blob/master/oom_adj.jpg "title")


