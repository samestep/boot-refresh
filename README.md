# boot-refresh

[![Clojars Project][clojars badge]][clojars]

[Boot] task to reload code using [`clojure.tools.namespace`][tools.namespace].

## Usage

In `build.boot`, add `boot-refresh` to your dependencies and `require` the task:

```clojure
(merge-env! :dependencies '[[samestep/boot-refresh "0.1.0" :scope "test"]])

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

## Development

To work on `boot-refresh` itself, you can use the provided `dev` task:

```sh
boot dev
```

This will start a watch loop that will reinstall the jar whenever one of
`boot-refresh`'s files changes.

[boot]: https://boot-clj.github.io/
[cider]: https://github.com/clojure-emacs/cider
[clojars]: https://clojars.org/samestep/boot-refresh
[clojars badge]: https://clojars.org/samestep/boot-refresh/latest-version.svg
[tools.namespace]: https://github.com/clojure/tools.namespace
