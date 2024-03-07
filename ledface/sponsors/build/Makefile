SOURCE := sponsors
STEP := 4
TOTAL_WIDTH := $(shell file $(SOURCE).png | sed 's/^.*image data, //' | sed 's/ .*$$//')
ADJ_WIDTH := $(shell expr $(TOTAL_WIDTH) - 32)
TOTAL_STEPS := $(shell expr $(ADJ_WIDTH) / $(STEP))

all:
	rm -f ../*.png
	for i in `seq --format="%03.0f" 0 $(shell expr $(TOTAL_STEPS) - 1)`; do convert $(SOURCE).png -crop 32x16+`expr $$i '*' $(STEP)`+0 ../$(SOURCE)_$$i.png; done
