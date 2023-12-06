PLAYBOOKS := $(sort $(wildcard private/*.yml))

all:	target/doorbell-1.0.jar $(PLAYBOOKS)
	ansible-playbook --ask-become-pass -i inventory $(PLAYBOOKS)

target/doorbell-1.0.jar:	$(wildcard src/main/java/org/riverbots/door/*.java)
	$(MAKE) clean
	mvn package

clean:
	rm -rf target
