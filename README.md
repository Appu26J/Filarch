<img width="360" alt="image" src="https://github.com/Appu26J/Filarch/assets/128838345/b6c8ff37-fb4d-4d6d-8172-448cd448dfd0">

A library for searching files very fast in Java.  
This can search the entire C drive **FIVE** times faster than File Explorer does!

### Example Usage
```java
SearchOptions searchOptions = new SearchOptions()
        .setParent(new File("C:\\"))
        .setIgnoreCase(true)
        .setSearchMode(SearchMode.CONTAINS)
        .setSearchHiddenFiles(true);

long time = System.currentTimeMillis();
Filarch.Result result = Filarch.searchFilesByName("a", searchOptions);
int count = result.getFiles().length;
System.out.println(result);
System.out.println("\n" + count + " total files found containing the letter a");
System.out.println("Took " + ((System.currentTimeMillis() - time) / 1000) + "s");
```

*There's also a method called ```Filarch.searchFilesByNameAsync``` for asynchronized file searching (without blocking the current thread), which is useful if you want live results.*

### Library Download
https://github.com/Appu26J/Filarch/releases/download/Filarch/filarch.jar
