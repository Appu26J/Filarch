package appu26j.filarch;

import java.io.File;

public class SearchOptions
{
    private File parent = new File(System.getProperty("user.home"));
    private boolean ignoreCase = false, hiddenFiles = true;
    private SearchMode searchMode = SearchMode.CONTAINS;
    private int code = 0;

    public SearchOptions setIgnoreCase(boolean ignoreCase)
    {
        this.ignoreCase = ignoreCase;
        return this;
    }

    public SearchOptions setSearchHiddenFiles(boolean hiddenFiles)
    {
        this.hiddenFiles = hiddenFiles;
        return this;
    }

    public SearchOptions setSearchMode(SearchMode searchMode)
    {
        this.searchMode = searchMode;
        return this;
    }

    public SearchOptions setParent(File parent)
    {
        this.parent = parent;
        return this;
    }

    public boolean isIgnoreCase()
    {
        return this.ignoreCase;
    }

    public boolean searchHiddenFiles()
    {
        return this.hiddenFiles;
    }

    public SearchMode getSearchMode()
    {
        return this.searchMode;
    }

    public File getParent()
    {
        return this.parent;
    }

    public int code()
    {
        if (this.code == 0)
        {
            String temp = this.parent.getAbsolutePath();
            int hash = 7;

            for (int i = 0; i < temp.length(); i++)
            {
                hash = hash * 31 + temp.charAt(i);
            }

            this.code = hash;
        }

        return this.code;
    }
}
