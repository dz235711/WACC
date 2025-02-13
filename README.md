This is the provided git repository for the WACC compilers lab. You should work
in this repository regularly committing and pushing your work back to GitLab.

# Provided files/directories

## src/main

The src/main directory is where you code for your compiler should go, and just
contains a stub hello world file with a simple calculator inside.

## src/test
The src/test directory is where you should put the code for your tests, which
can be ran via `scala-cli test .`. The suggested framework is `scalatest`, the dependency
for which has already been included.

## project.scala
The `project.scala` is the definition of your project's build requirements. By default,
this skeleton has added the latest stable versions of both `scalatest` and `parsley`
to the build: you should check **regularly** to see if your `parsley` needs updating
during the course of WACC!

## compile

The compile script can be edited to change the frontend interface to your WACC
compiler. You are free to change the language used in this script, but do not
change its name.

## Makefile

Your Makefile should be edited so that running 'make' in the root directory
builds your WACC compiler. Currently running 'make' will call
`scala --power package . --server=false --jvm system --graalvm-jvm-id graalvm-java21 --native-image --force -o wacc-compiler`, producing a file called
`wacc-compiler`
in the root directory of the project. If this doesn't work for whatever reason, there are a few
different alternatives you can try in the makefile. **Do not use the makefile as you're working, it's for labts/CI!**

# Contribution guidelines

## Git hooks
We have a pre-commit hook and a commit-msg hook. To install them, run the following:
```shell
cp hooks/pre-commit .git/hooks/pre-commit
cp hooks/commit-msg .git/hooks/commit-msg
chmod +x .git/hooks/pre-commit
chmod +x .git/hooks/commit-msg
```

## Commit messages

Commit messages should be of the form `tag:description`. These tags could be any of the following:
- `feat`
- `chore`
- `ci`
- `refactor`
- `perf`
- `fix`
- `doc`
- `test`
- `wip`
- `revert`
- `style`

The description should be imperative and lowercase, with no full-stop.

Branch names should have the format `tag/description`, e.g., `feat/combinate-parsers`, where we have dashes between words (kebab case).

Co-authors should be added to commits when pair-programming, in the format:
```
[Commit Message]

Co-authored-by: [Name] <[Email]>
```

## Compiling
To compile WACC locally, run the following command:
```shell
scala --power package . -o wacc-compiler -f
```

## Running
To run WACC locally, run the following command:
```shell
./compile [args]
```
(The `--help` flag will show you the available options)