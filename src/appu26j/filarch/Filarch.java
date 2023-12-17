package appu26j.filarch;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Filarch
{
    private static final HashMap<Integer, Result> tempObjects = new HashMap<>();

    public static Result searchFiles(SearchOptions searchOptions)
    {
        return searchFilesByName("", searchOptions);
    }

    public static Result searchFilesByName(String name, SearchOptions searchOptions)
    {
        Result result = searchFilesByNameAsync(name, searchOptions);

        while (result.isSearching())
        {
            ;
        }

        return result;
    }

    public static Result searchFilesAsync(SearchOptions searchOptions)
    {
        return searchFilesByNameAsync("", searchOptions);
    }

    public static Result searchFilesByNameAsync(String name, SearchOptions searchOptions)
    {
        if (tempObjects.containsKey(searchOptions.code()))
        {
            Result result = tempObjects.get(searchOptions.code());

            if (result.isSearching())
            {
                return result;
            }

            else
            {
                tempObjects.remove(searchOptions.code());
            }
        }

        Result result = new Result();
        tempObjects.put(searchOptions.code(), result);
        search(searchOptions.getParent(), name, searchOptions.isIgnoreCase(), searchOptions.searchHiddenFiles(), searchOptions.getSearchMode(), searchOptions.code());
        return result;
    }

    private static void search(File file, String name, boolean ignoreCase, boolean hiddenFiles, SearchMode searchMode, int code)
    {
        if (!hiddenFiles && !file.getName().isEmpty() && file.isHidden())
        {
            return;
        }

        if (file.isDirectory() && file.listFiles() != null)
        {
            Threads.addThread(() ->
            {
                try
                {
                    for (File f : Objects.requireNonNull(file.listFiles()))
                    {
                        search(f, name, ignoreCase, hiddenFiles, searchMode, code);
                    }
                }

                catch (Exception e)
                {
                    ;
                }
            });
        }

        else
        {
            String fileName = file.getName(), finalStartsWith = name;

            if (ignoreCase)
            {
                finalStartsWith = finalStartsWith.toLowerCase();
                fileName = fileName.toLowerCase();
            }

            try
            {
                switch (searchMode)
                {
                    case STARTS_WITH:
                    {
                        if (fileName.startsWith(finalStartsWith))
                        {
                            tempObjects.get(code).addFile(file);
                        }

                        break;
                    }

                    case CONTAINS:
                    {
                        if (fileName.contains(finalStartsWith))
                        {
                            tempObjects.get(code).addFile(file);
                        }

                        break;
                    }

                    case ENDS_WITH:
                    {
                        if (fileName.endsWith(finalStartsWith))
                        {
                            tempObjects.get(code).addFile(file);
                        }

                        break;
                    }
                }
            }

            catch (Exception e)
            {
                ;
            }
        }
    }

    private static class Threads
    {
        private static final ArrayList<Thread> threads = new ArrayList<>();

        public static void addThread(Runnable runnable)
        {
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }

        public static boolean haveFinished()
        {
            boolean done = true;

            for (int i = 0; i < threads.size(); i++)
            {
                Thread thread = threads.get(i);

                if (thread != null && thread.isAlive())
                {
                    done = false;
                    break;
                }
            }

            return done;
        }
    }

    public static class Result
    {
        private final ArrayList<File> files = new ArrayList<>();

        public File[] getFiles()
        {
            ArrayList<File> temp = new ArrayList<>(this.files);
            temp.removeIf(Objects::isNull);
            return temp.toArray(new File[0]);
        }

        public void addFile(File file)
        {
            this.files.add(file);
        }

        public boolean isSearching()
        {
            return !Threads.haveFinished();
        }

        @Override
        public String toString()
        {
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<File> temp = new ArrayList<>(this.files);

            temp.forEach(file ->
            {
                if (file != null)
                {
                    stringBuilder.append(file.getName()).append(", ");
                }
            });

            if (stringBuilder.length() > 2)
            {
                return "Result@[" + stringBuilder.substring(0, stringBuilder.length() - 2) + "]";
            }

            else
            {
                return "Result@[]";
            }
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Result)
            {
                Result result = (Result) obj;
                return Arrays.equals(result.getFiles(), this.getFiles());
            }

            return super.equals(obj);
        }
    }
}
