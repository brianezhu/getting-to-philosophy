# Getting to Philosophy

This sample application attempts to get to the [Philosophy](https://en.wikipedia.org/wiki/Philosophy) page of Wikipedia. 

It does so by following the rules laid out in [https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy](https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy).

I've found in my testing that many searches do not successfully get to Philosophy. Instead, the search ends up in an infinite loop (the Mathematics page seems especially popular in these cases). If a loop occurs, the application will automatically stop searching.

The application uses an H2 in-memory database. The schema is defined in <code>javax.persistence</code> entities.

## Endpoints

### <code>POST</code> philosophy

Parameters:

- **startingPath** _(required)_ — The page from which you'd like to begin your search.

This endpoint will return a JSON object containing:

- **completed** - whether the search completed (without errors)
- **status** - final outcome of the search
- **numOfHops** - number of hops it took to finish the search. If the search ended with a loop, it will include the final page (duplicate page) in the count.
- **path** - An array of the path which the search took.

### Examples:

**Request which successfully found Philosophy**

``` 
POST /philosophy
{
	"startingPath": "https://en.wikipedia.org/wiki/Behavior"
}
```

**Return**
``` 
200 OK
{
	"completed": "completed",
	"status": "success",
	"numOfHops": 3,
	"path":[
		"https://en.wikipedia.org/wiki/Behavior",
		"https://en.wikipedia.org/wiki/Action_(philosophy)",
		"https://en.wikipedia.org/wiki/Philosophy"
	]
}
```

**Request with unsuccessful search**

``` 
POST /philosophy
{
	"startingPath": "https://en.wikipedia.org/wiki/Artistotle"
}
```

**Return**
``` 
200 OK
{
	"completed": "completed",
	"status": "failed_loop_detected",
	"numOfHops": 17,
	"path":[
		"https://en.wikipedia.org/wiki/Artistotle",
		"https://en.wikipedia.org/wiki/Ancient_Greece",
		"https://en.wikipedia.org/wiki/Civilization",
		"https://en.wikipedia.org/wiki/Complex_society",
		"https://en.wikipedia.org/wiki/Anthropology",
		"https://en.wikipedia.org/wiki/Human",
		"https://en.wikipedia.org/wiki/Extant_taxon",
		"https://en.wikipedia.org/wiki/Biology",
		"https://en.wikipedia.org/wiki/Natural_science",
		"https://en.wikipedia.org/wiki/Branches_of_science",
		"https://en.wikipedia.org/wiki/Formal_science",
		"https://en.wikipedia.org/wiki/Formal_language",
		"https://en.wikipedia.org/wiki/Mathematics",
		"https://en.wikipedia.org/wiki/Quantity",
		"https://en.wikipedia.org/wiki/Counting",
		"https://en.wikipedia.org/wiki/Element_(mathematics)",
		"https://en.wikipedia.org/wiki/Mathematics"
	]
}
```

**Request with errors**

``` 
POST /philosophy
{
	"startingPath": ""
}
```

**Return**
``` 
200 OK
{
	"completed": "did_not_complete",
	"status": "Invalid URL",
	"numOfHops": -1,
	"path": null
}
```


## Areas to improve

If I had longer, I'd add the following:

- Additional integration tests.
- Adding unit tests.
- Better error reporting.
- Caching search paths, so we don't have to recompute paths which we have already crawled before
- 40X HTTP codes for failed searches.
- I've read there's a more reliable method of reaching Philosophy. Basically, you visit the 2nd "main text" link if you've already visited the 1st "main text" link (loop). I'd consider implementing this method for loops.
