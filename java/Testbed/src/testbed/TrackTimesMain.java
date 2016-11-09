package testbed;

import java.io.*;
import java.util.*;

public class TrackTimesMain
{
    /**
     * @param args
     */
    public static void main( String[] args )
    {
        if( args.length != 2 )
        {
            printUsage();
            return;
        }

        File inFile = new File( args[0] );
        File peopleFile = new File( args[1] );

        List<Person> people;
        List<RunTime> times;

        try
        {
            times = readTimes( inFile );
            System.out.println( times.size() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            return;
        }

        Collections.sort( times, new AgeSorter() );
        printTimes( times );

        try
        {
            people = readPeople( peopleFile );
            System.out.println( people.size() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            return;
        }

        listMatches( people, times );
    }

    private static void printTimes( List<RunTime> times )
    {
        for( RunTime rt : times )
        {
            Integer age = rt.getAge();
            if( age > 25 && age < 35 )
                System.out.println( rt.toString() );
        }
    }

    private static void listMatches( List<Person> people, List<RunTime> times )
    {
        for( Person p : people )
        {
            for( RunTime rt : times )
            {
                if( p.isName( rt.name ) )
                {
                    System.out.println( "> " + p.name + " < " + rt.name );
                }
            }
        }
    }

    private static List<Person> readPeople( File inFile ) throws IOException
    {
        List<Person> people = new ArrayList<Person>();

        try( FileReader fr = new FileReader( inFile );
             BufferedReader br = new BufferedReader( fr ) )
        {
            String line;
            while( ( line = br.readLine() ) != null )
            {
                Person p = new Person();

                p.name = line.trim();

                people.add( p );
            }
        }

        return people;
    }

    private static List<RunTime> readTimes( File inFile ) throws IOException
    {
        List<RunTime> times = new ArrayList<RunTime>();

        try( FileReader fr = new FileReader( inFile );
             BufferedReader br = new BufferedReader( fr ) )
        {
            String line;
            while( ( line = br.readLine() ) != null )
            {
                RunTime rt = new RunTime();

                rt.name = line.substring( 12, 32 );
                rt.name = rt.name.trim();

                rt.age = line.substring( 32, 34 );
                rt.age = rt.age.trim();

                rt.city = line.substring( 35, 48 );
                rt.city = rt.city.trim();

                rt.state = line.substring( 48, 50 );
                rt.state = rt.state.trim();

                rt.time = line.substring( 53 );
                rt.time = rt.time.trim();

                times.add( rt );
            }
        }

        return times;
    }

    private static void printUsage()
    {
        ;
    }

    private static class Person
    {
        public String name;

        public boolean isName( String otherName )
        {
            String[] theseNames = name.split( " " );
            String[] thoseNames = otherName.split( " " );
            return getNumMatches( theseNames, thoseNames ) > 1;
        }

        private static int getNumMatches( String[] theseNames,
            String[] thoseNames )
        {
            int numMatches = 0;

            for( String aname : theseNames )
            {
                for( String bname : thoseNames )
                {
                    if( aname.compareToIgnoreCase( bname ) == 0 )
                    {
                        numMatches++;
                    }
                }
            }

            return numMatches;
        }
    }

    private static class AgeSorter implements Comparator<RunTime>
    {
        @Override
        public int compare( RunTime thisRt, RunTime thatRt )
        {
            return thisRt.getAge().compareTo( thatRt.getAge() );
        }
    }

    private static class RunTime
    {
        public String name;
        public String age;
        public String city;
        public String state;
        public String time;

        public Integer getAge()
        {
            return Integer.parseInt( age );
        }

        @Override
        public String toString()
        {
            return String.format( "%s, %s, %s, %s, %s", name, age, city, state,
                time );
        }
    }
}
