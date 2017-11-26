# hacker-news
simple app to load hacker-news


# Guide to generate code coverage

Note
=======================================
Instrumented test will rely on real data from internet, so please make sure the device/emulator have stable internet access when runing instrumented test.





Step 1
=======================================
make sure you have a emulator running or a device connecting to the pc





Step 2
=======================================
code coverage can be generated with command line or android studio


Using command line
=======================================
1. go to the project folder
cd HackerNews

2. clean and generate test report and code coverage report

...............for Mac/Linux.......................
./gradlew clean jacocoTestReport

...............for Windows.........................
gradlew clean jacocoTestReport


Using android studio - command line
=======================================
1. open the project with android studio

2. open "Terminal" at the buttom, run following command

...............for Mac/Linux.......................
./gradlew clean jacocoTestReport

...............for Windows.........................
gradlew clean jacocoTestReport


Using android studio - GUI
=======================================
1. open the project with android studio

2. open the "Gradle projects" panel from the "Gradle" lable at the right side, browse to HaskNews-Tasks-other-jacocoTestReport-double click to run it





Step 3
=======================================
The reports are locating in below folders
Unit test report                             >>>>>>>>>>>>>>>>>>> HackerNews\app\build\reports\tests\testDebugUnitTest
Instrumented test report                     >>>>>>>>>>>>>>>>>>> HackerNews\app\build\reports\androidTests\connected
code coverage report(instrument test only)   >>>>>>>>>>>>>>>>>>> HackerNews\app\build\reports\coverage\debug
code coverage report(overall)                >>>>>>>>>>>>>>>>>>> HackerNews\app\build\reports\jacoco\jacocoTestReport\html