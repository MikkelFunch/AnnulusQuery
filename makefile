#this is a sucky makefile, but it works our purposes

.PHONY: compile test main.test.%
testclasses=$(shell cd bin; find main -path "main/test/*.class" | sed -e 's/\.class//g' -e 's/\//\./g')

default: compile

compile:
	$(shell find -name '*.java' -exec javac -d bin -cp bin:lib/\* {} +)

test: $(testclasses)

main.test.%: compile
	java -cp bin:lib/\* org.junit.runner.JUnitCore $@
