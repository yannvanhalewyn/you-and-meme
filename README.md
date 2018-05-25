# You and meme

A [re-frame](https://github.com/Day8/re-frame) application made to help a friend of mine in art school. It will spin of a web server which show a button, when that button is pressed it will pick a random video from youtube and download it. The project would involve her live VJ'ing random internet video's,  and the audience would trigger the downloading / selecting of those new random videos.

## Development Mode

### Run application:

```
lein repl
=> (go)
```

Wait a bit, then browse to [http://localhost:8080](http://localhost:8080).

This will start the web server, chsk router and figwheel will push any cljs changes to the browser.

## Production Build


To compile clojurescript to javascript:

```
lein uberjar
```

Will produce a standalone jar in `target/you_and_meme.jar`
