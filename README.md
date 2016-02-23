# boot-postcss

[![Clojars Project](http://clojars.org/bendyorke/boot-postcss/latest-version.svg)](http://clojars.org/cpmcdaniel/boot-copy)

A boot plugin to run your css through PostCSS & use css modules.

[](dependency)
```clojure
[bendyorke/boot-postcss "0.0.1"] ;; latest release
```
[](/dependency)

# Why?

Coming from JavaScript, one of the tools I enjoy most is css-modules.  They make css _so_ easy to work with.  However, there is no clojure tooling for this front end wonder!

# When?

As you can see from the version number, this is still a work in progress!  I've outlined what I'm aiming to have for v1.0.0.  If there is something you would like to see in v1.0, if you'd like to contribute, or if you're just curious head to the issues tab to see more.

# How?

Currently `boot-postcss` will run any `.css` file in your directory path through a prefefined set of postcss plugins (these will be editable soon!).  You can pass two options: `-o <filename>` and `-m`.  More information about the options can be found below or by running `boot postcss -h`.

```
-o, --output-filename NAME  Set file to concat all styles into.  If none is provided,
                              each css file will be written seperately to NAME.
-m, --modules               Enable CSS Modules
```
