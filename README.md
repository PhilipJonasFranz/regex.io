# regex.io [![version](https://img.shields.io/badge/version-1.0.0-green.svg)](https://semver.org)

## How to use

Include the standalone .jar in your project folder and you are ready to go. With

`String regex = new Regex().anyDigit().build()` 

you can create a regex via the builder pattern. By calling `build()` on the regex object, you can convert the object to a string that represents this regex operation. Using `string.matches(regex)` you can match any string against the created pattern.

If you want additional safety when creating patterns, turn on runtime assertions, to make sure the passed arguments are valid.

## License and Copyright
 © Philip Jonas Franz
 
 Licensed under [Apache License 2.0](LICENSE). 