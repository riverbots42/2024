TESTFILES := Frame.java Animation.java AnimationList.java

runtest:	Main.class AnimationList.java
	rm -rf $(foreach I,$(TESTFILES),test/$(I)) test/*.class test/frc
	cp $(TESTFILES) test/
	cd test && javac *.java
	cd test && mkdir -p frc/robot && mv *.class frc/robot && java frc.robot.Main

AnimationList.java:	Main.class AnimationList.tmpl
	java Main > $@

Main.class:	Main.java
	javac $^

clean:
	rm -rf *.class AnimationList.java $(foreach I,$(TESTFILES),test/$(I)) test/frc
