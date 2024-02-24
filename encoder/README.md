# The Stupid Ridiculously-Cheap Drive Encoder

(aka "StuRCDE," pronounced "Sturk-Dee")

## Introduction

A drive encoder is a device that "clicks" whenever a motor or wheel turns, to give the controller an idea where the robot is either in e.g. the rotation of an arm or similar or where the bot is on the field (so we can do autonomous driving).

The big problems with industrial encoders such as the
[US Digital E4T](https://www.andymark.com/products/e4t-oem-miniature-optical-encoder-kit) are that:

1. They're expensive ($40-60 each, and you need at least two of them to track your playfield position).
2. They're not reusable (once attached to a gearbox, removing them effectively destroys them).
3. They're supply-constrained (ordering new ones takes FOREVER through typical channels).

The StuRCDE gets rid of this problem by:

1. Being stupid-simple (and therefore cheap).  Its design consists of two resistors and a rotary car stereo-style encoder switch.
2. Being stupid-simple (and therefore disposable).  Being $100 cheaper than the industrial solutions, who cares if we can't reuse them?
3. Being stupid-simple (and therefore easy-to-source).  So we can order all parts from Amazon or any other online vendor, not just e.g. AndyMark.

## Contents

sturcde/ - The printed circuit board version of this project, using pin headers to connect to the physical wiring.  This is OPTIONAL but makes assembly nicer if one is willing to source boards from a PCB fab like [PCBWay](https://pcbway.com).
sturcde-overview.png - An overview of the whole works and its theory of operation from a top level.
sturcde-wiring.png - How to make one of these without the PCB; just a bit of wire and some solder along with the three components.
