# Prometheus metrics-audio

A Kotlin project that adds sound to a set of monitored Prometheus metrics.

## Quickstart

```shell
docker-compose -f docker/docker-compose.yaml up
./gradlew run
```

Q: Why do I hear nothing?

A: You may need to turn up the sound


## Summary

### Audio 

There are 2 possible audio types currently: Metro and Synth. 
- Metro is short for metronome, it is a periodic sound similar to a heart rate monitor in hospital
- Synth is short for synthesizer, it is a constant sound with some pink noise mixed in. 
  It plays on alternating channels, so you will probably hear it on only one side of your audio output.

### Config

You can find the full config definitions and defaults in [the Config file](./src/main/kotlin/util/Config.kt).

A config file is roughly the equivalent of a Grafana dashboard, it's a collection of Prometheus metrics that are being output as sound.


## Future goals / Areas for improvement
- Scrape multiple times within a run and support a scrape_interval config
- Consider switching to a server/client arch and playing sound via Javascript in the browser
- Study hearing theory and add explicit failsafes to protect people's hearing at all times
- Allow loading multiple configs and switching between them dynamically

This project is licensed under the MIT License.
