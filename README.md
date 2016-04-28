# avenir

Clojure utilities which may find a proper home in the [future](http://www.larousse.fr/dictionnaires/francais-anglais/avenir/6998)

[![Clojars Project](https://img.shields.io/clojars/v/avenir.svg)](https://clojars.org/avenir) [![Build Status](https://travis-ci.org/tmarble/avenir.png?branch=master)](https://travis-ci.org/tmarble/avenir)

## Usage

See the [API Docs](http://tmarble.github.io/avenir/doc/api/)

Follow changes in the [CHANGELOG](CHANGELOG.md)

Check out these examples!
* [avenir-clj](examples/avenir-clj) Using avenir in a Clojure project
 * **boot run** will run the example on the command line
 * **boot build** will build an uberjar you can run: `java -jar target/avenir-clj.jar`
* [avenir-cljs](examples/avenir-cljs) Using avenir in a ClojureScript project
 * **boot run** will run the example you can open in your brower `open http://localhost:3000`
* [avenir-script](examples/avenir-script/avenir-script) Using avenir in a boot script
 * `./avenir-script` will run the example


A note about ClojureScript: these functions from Clojure need to be
referred to as follows:
 * The function `float?` must be included from `[cljs.pprint :refer [float?]]`
 * The function `format` must be included from `[avenir.utils :refer [format]]`
   (see [CLJS-324](http://dev.clojure.org/jira/browse/CLJS-324))
 * When used with ClojureScript **avenir** targets browsers. This is
   important to know because many of the math functions depend on the
   availability of the [Math](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math) object which is not available in PhantomJS,
   Node or [other JavaScript environments](https://github.com/bensu/doo#setting-up-environments).

## Development

The avenir library uses [boot](http://boot-clj.com/) as a build tool. For
more on boot see [Sean's blog](http://seancorfield.github.io/blog/2016/02/02/boot-new/) and the [boot Wiki](https://github.com/boot-clj/boot/wiki).

You can get help for all available boot tasks with `boot -h`

These are custom tasks to help development:
 * **clj-dev** Clojure REPL for CIDER
   This will start a REPL that you can use with [CIDER](https://github.com/clojure-emacs/cider). You will see output like this:

````
$ boot clj-dev
nREPL server started on port 42328 on host 127.0.0.1 - nrepl://127.0.0.1:42328
````

Now you can connect to that port with `M-x cider-connect`. (Or if you
want to simplify the process make a keybinding to my function `my-cider-connect`
which I have proposed as an [enhancement](https://github.com/clojure-emacs/cider/issues/1580) to CIDER). Now you can play with the library interactively:

````
boot.user> (in-ns 'avenir.utils)
#namespace[avenir.utils]
avenir.utils> (keywordize {"one" "one" 2 "two" 'three "three"})
{:one "one", :2 "two", :three "three"}
avenir.utils>
````

If you change a source file run `M-x cider-eval-buffer` for
the changes to be reflected in the REPL.

You can use `C-c C-q` to quit CIDER and then `^C` to stop the nREPL server.

 * **cljs-dev** ClojureScript Browser REPL for CIDER
   This will start a Browser REPL that you can use with [CIDER](https://github.com/clojure-emacs/cider), like above. You will see output like this:

````
$ boot cljs-dev
Starting reload server on ws://localhost:34221
Writing boot_reload.cljs...
Writing boot_cljs_repl.cljs...
Sifting output files...
2016-02-22 17:15:25.145:INFO::clojure-agent-send-off-pool-0: Logging initialized @11008ms
2016-02-22 17:15:25.201:INFO:oejs.Server:clojure-agent-send-off-pool-0: jetty-9.2.10.v20150310
2016-02-22 17:15:25.228:INFO:oejs.ServerConnector:clojure-agent-send-off-pool-0: Started ServerConnector@435e4a5b{HTTP/1.1}{0.0.0.0:3000}
2016-02-22 17:15:25.229:INFO:oejs.Server:clojure-agent-send-off-pool-0: Started @11093ms
Started Jetty on http://localhost:3000

Starting file watcher (CTRL-C to quit)...

Adding :require adzerk.boot-reload to app.cljs.edn...
nREPL server started on port 41058 on host 127.0.0.1 - nrepl://127.0.0.1:41058
Adding :require adzerk.boot-cljs-repl to app.cljs.edn...
Compiling ClojureScript...
• js/app.js
Writing target dir(s)...
Elapsed time: 34.950 sec
````
Now you can connect to CIDER and then start the Browser REPL.
After `(start-repl)` don't forget to open [http://localhost:3000](http://localhost:3000)
to connect to the browser!

````
boot.user> (start-repl)
<< started Weasel server on ws://127.0.0.1:42922 >>
<< waiting for client to connect ... Connection is ws://localhost:42922
Writing boot_cljs_repl.cljs...
 connected! >>
To quit, type: :cljs/quit
nil
cljs.user> (in-ns 'avenir.utils)
nil
avenir.utils> (keywordize {"one" "one" 2 "two" 'three "three"})
{:one "one", :2 "two", :three "three"}
avenir.utils> (enable-console-print!)
nil
avenir.utils> (println "Hello!")
nil
````

Simply save changes to the source files to have them reloded automatically in the browser.

You can use `:cljs/quit` to quit the browser REPL, `C-c C-q` to quit CIDER and then `^C` to stop the nREPL server.

 * **build** Build jar and install to local repo
 * **build-target** Build the project locally as a JAR. *NOTE*: the artifacts will be saved in the `target` directory for inspection.

## Testing

* **test** Run tests with Clojure
* **tests** Run tests with ClojureScript
 * The JavaScript environment `:js-env` is set to `:phantom` by default. You must install [PhantomJS](http://phantomjs.org/) for this to work. (*NOTE* certain `avenir.math` tests will be skipped when running in PhantomJS)
 * In order to test in browsers like Firefox (`:js-env :firefox`) you
   will also need to install
   [Karma](http://karma-runner.github.io/0.13/index.html).
 * On [Debian](http://www.debian.org) systems:

````
apt-get install npm
npm install -g karma karma-cljs-test karma-cli karma-firefox-launcher
````

* **testc** Run tests with Clojure & ClojureScript
 * Both **tests** and **testc** run with PhantomJS by default, but you
   can run with Firefox with `boot testc -e firefox`

## Copyright and license

Copyright © 2016 Tom Marble

Licensed under the [MIT](http://opensource.org/licenses/MIT) [LICENSE](LICENSE)
