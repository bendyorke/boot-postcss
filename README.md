# boot-postcss

A boot plugin to run postcss as part of your buildstep.  Currently under development.

# Why?

Coming from JavaScript, one of the tools I enjoy most is css-modules.  They make css _so_ easy to work with.  However, there is no clojure tooling for this front end wonder!

# What?

This seems like a simple matter, but there are some bigger issues at play.  Primarily:

- Boot tasks need to be written in Clojure (not ClojureScript)
- CSS Modules rely on PostCSS - a node based css parser
- JavaScript must be interfaced via Nashorn wich is interfaced via Java which is interfaced via Clojure
- Node API's are not available in Java 8's Nashorn, which PostCSS requires

There are some more, open ended features that I'm still working on:

- How can you require the css files (or class names) in Clojure(Script) files?
- How can you use PostCSS plugins with this boot loader?
- How does the css get on the page?

# How?

Let me start by saying I have no expirience in Java, Clojure, or hardly even ClojureScript for that matter.  That being said, my initial approach is something like:

- CSS Modules uses a postcss to parse CSS.
- PostCSS is written in JavaScript.
- browserify can be used to compile javascript files down to a single, interoperable file.
- JavaScript can be interfaced with in Java via Java 8's Nashorn.
- Clojure can interface with Java.
