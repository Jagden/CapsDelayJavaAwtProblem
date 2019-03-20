# CapsDelayJavaAwtProblem
The project creates and patches Java to address the Linux caps-lock delay problems and attempt to fix them for Java UI applications.

There are many threads on the Internet on the subject.
I personally an old typer that types very fast and uses Caps lock a lot to uppercase the first letter.
The claim of please use shift to uppercase letters is not working for me ! :) 
The bug in Linux causes typing of a word to appear like: HEllo instead of Hello.
The workarounds in the market do solve the problem, however, for Java, UI apps such as IntelliJ still suffer from it.

# What happens in Java that is different ?
Java uses XLib to open the Keyboard via `XGrabKeyboard` and then pull events via `XNextEvent` and process it in
`XToolKit, XlibWrapper, XKeysym` and generally in the `sun.awt.X11` package.
In `XKeysum.getKeysym` Java looks on the key lock modifies and due to the Linux bug, it capitalizes the typed character
even when caps was released when it really shouldn't.
To address this, this project patches rt.jar in the Java JRE to fix the problem. 

# Information on the web

[Ubuntu xorg caps lock delay bug 1376903](https://bugs.launchpad.net/ubuntu/+source/xorg-server/+bug/1376903)

[Github Linux CapsLock Delay Fixer](https://github.com/HexValid/Linux-CapsLock-Delay-Fixer)

[freedesktop.org bug report 27903](https://bugs.freedesktop.org/show_bug.cgi?id=27903)

[A patch done long time ago to Linux to enable the workarounds above](https://patchwork.freedesktop.org/patch/6538/)

[IntelliJ complaint regarding the issue](https://intellij-support.jetbrains.com/hc/en-us/community/posts/115000370770-Caps-Lock-Delay-Problem-Ubuntu-)
