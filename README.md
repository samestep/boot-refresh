# boot-refresh

[Boot] task to reload code using [`clojure.tools.namespace`][tools.namespace].

## Usage

In `build.boot`, add `boot-refresh` to your dependencies and `require` the task:

```clojure
(merge-env!
 :dependencies '[[samestep/boot-refresh "0.1.0-SNAPSHOT" :scope "test"]])

(require '[samestep.boot-refresh :refer [refresh]])
```

You can view the help info for the `refresh` task from the command line:

```sh
boot refresh -h
```

The `refresh` task works best with [CIDER]:

```lisp
(setq cider-boot-parameters "repl -s watch refresh")

(cider-jack-in)
```

If you modify any of your source files and save your changes, you should be able
to immediately use the new code from your REPL.

[boot]: http://boot-clj.com/
[cider]: https://github.com/clojure-emacs/cider
[tools.namespace]: https://github.com/clojure/tools.namespace
