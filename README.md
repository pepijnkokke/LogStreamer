# Distributed Systems – LogStreamer 1.0

![Fishermen on a Log Raft](https://github.com/pepijnkokke/LogStreamer/raw/master/logo.jpg)

### If you are just browsing this repository...

It's just homework, nothing interesting to see here.

### If you are marking this assignment...

I've used `src` instead of `source`, because it tends to make Maven
and IntelliJ Idea happier when I do this. The top-level folder is my
Idea project folder, this means that some files (`LogStreamer.iml` and
`pom.xml`) are present in the top-level folder.

If you run into problems with my code, I'm guessing the most likely
option is that you are using plain text files, where I am using the
gzipped files taken directly from:
<https://dumps.wikimedia.org/other/pageviews/2016/2016-10/>

If you care about which XML config file Ignite uses, then there is a
`--config=` option built into the executable JAR file (see `run.sh`).
Make sure that you've set `IGNITE_HOME` if you want to have any chance
of it finding your config file, though...
  
Right. Last few things. The assignment specifies that the query node
in `part A` should check every 10 seconds, which it does. The query
note in `part B` should check _continuously_, which it does. The query
note in `part C` should do the same as the one in `part B`. This it
does too. But I do have my doubts, because _continuously_ is often,
so just in case, you can easily edit this in run.sh (`--query-every=`).

Yes. Adding an image was necessary.
