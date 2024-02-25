DIRS := $(foreach I,$(wildcard *.gif),$(subst .gif,,$(I)))

all:	$(wildcard *.gif) Main.class
	java Main *.gif

Main.class:	Main.java ImageFrame.class
	javac $<

ImageFrame.class:   ImageFrame.java
	javac $<

clean:
	rm -rf *.class $(DIRS)
