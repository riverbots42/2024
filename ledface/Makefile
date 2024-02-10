TESTFILES := Frame.java Animation.java Animations.java

runtest:	Main.class Animations.java
	rm -f $(foreach I,$(TESTFILES),test/$(I)) test/*.class test/frc
	cp $(TESTFILES) test/
	cd test && javac *.java
	cd test && mkdir -p frc/robot && mv *.class frc/robot && java frc.robot.Main

Animations.java:	Main.class Animations.tmpl
	java Main > $@

Main.class:	Main.java
	javac $^

clean:
	rm -f *.class Animations.java $(foreach I,$(TESTFILES),test/$(I))
