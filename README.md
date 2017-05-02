# SuperInvoker
Fuck me
###### Step 1
* maven { url "https://jitpack.io" }
* compile 'com.github.zdxiang:SuperInvoker:the lasted version'

###### Step 2
* Make sure that Your Application extends DaemonApplication and overried the methods
* Init the invokerEngin in 'startYourService'
* Return your working processName in 'getYourProcessName'.E.g cn.zdxiang.test:process
* Return your empty processName in 'getCoreProcessName'.E.g cn.zdxiang.test:coreprocess
* Return your working service CanonicalName that extends BaseBizService in 'getYourServiceCanonicalName'.E.g  MyService.class.getCanonicalName();

###### Step 3
* Set your manifest file
