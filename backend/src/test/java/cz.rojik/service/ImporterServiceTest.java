package cz.rojik.service;

import cz.rojik.MBMarkApplicationTest;
import cz.rojik.utils.pojo.ImportsResult;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ImporterServiceTest extends MBMarkApplicationTest {

    @Autowired
    private ImporterService importerService;

    private ImportsResult imports;

    @Before
    public void setUp() throws Exception {
        imports = new ImportsResult();
    }

    @Test
    public void getLibrariesToImportTest1() {
        String input = "java.util.List<Integer> arrayList;\n" +
                "int[] array;\n" +
                "java.util.Random random;\n" +
                "random = new Random();\n" +
                "array = new int[1000];\n" +
                "arrayList = new ArrayList<>();\n" +
                "for (int i = 0; i < 1000; i++) {\n" +
                "int randomNumber = random.nextInt();\n" +
                "array[i] = randomNumber;\n" +
                "arrayList.add(new Integer(randomNumber));\n" +
                "}";

        imports = importerService.getLibrariesToImport(imports, input);

        assertTrue(imports.getLibraries().contains("java.util.List"));
        assertTrue(imports.getLibraries().contains("java.util.ArrayList"));
        assertTrue(imports.getLibraries().contains("java.util.Random"));
        assertEquals(imports.getLibraries().size(), 3);
    }

    @Test
    public void getLibrariesToImportTest2() {
        String input = "String generatedID = UUID.randomUUID().toString();\n" +
                "ClassLoader classLoader = getClass().getClassLoader();\n" +
                "File srcDir = new File(classLoader.getResource().getFile());\n" +
                "File destDir = new File();";

        imports = importerService.getLibrariesToImport(imports, input);

        assertTrue(imports.getLibraries().contains("java.util.UUID"));
        assertTrue(imports.getLibraries().contains("java.io.File"));
        assertEquals(imports.getLibraries().size(), 2);
    }

    @Test
    public void getLibrariesToImportTest3() {
        String input = "String[] hrany = new String[3];\n" +
                "\t\tString[] sousedi = null;\n" +
                "\t\tif(hrana.charAt(1) == '-') {\n" +
                "\t\t\tsousedi = hrana.split(\"-\");\n" +
                "\t\t}\n" +
                "\t\telse if(hrana.charAt(1) == '>') {\n" +
                "\t\t\tsousedi = hrana.split(\">\");\n" +
                "\t\t}\n" +
                "\t\telse {\n" +
                "\t\t\treturn null;\n" +
                "\t\t}\n" +
                "\t\tfor (int i = 0; i<2; i++) {\n" +
                "\t\t\thrany[i] = sousedi[i];\n" +
                "\t\t}\n" +
                "\t\thrany[2] = hrana.charAt(1)+\"\";\n" +
                "\t\treturn hrany;\n" +
                "Integer abs = Math.abs(5)\n" +
                "java.util.List hash = new LinkedList<>();";

        imports = importerService.getLibrariesToImport(imports, input);

        assertTrue(imports.getLibraries().contains("java.util.List"));
        assertTrue(imports.getLibraries().contains("java.util.LinkedList"));
        assertEquals(imports.getLibraries().size(), 2);
    }

    @Test
    public void getLibrariesToImportTest4() {
        String input = "int oldSize = size;\n" +
                "        int index = Math.abs(obj.hashCode()) % con.length;\n" +
                "        if(con[index] == null)\n" +
                "            con[index] = new LinkedList<E>();\n" +
                "        if(!con[index].contains(obj)) {\n" +
                "            con[index].add(obj);\n" +
                "            size++;\n" +
                "            \n" +
                "        }\n" +
                "        if(1.0 * size / con.length > LOAD_FACTOR_LIMIT)\n" +
                "            resize();\n" +
                "        return oldSize != size;\n" +
                "UnsortedHashSet<E> temp = new UnsortedHashSet<E>();\n" +
                "        temp.con = (LinkedList<E>[])(new LinkedList[con.length * 2 + 1]);\n" +
                "        for(int i = 0; i < con.length; i++){\n" +
                "            if(con[i] != null)\n" +
                "                for(E e : con[i])\n" +
                "                    temp.add(e);\n" +
                "        }\n" +
                "        con = temp.con;";

        imports = importerService.getLibrariesToImport(imports, input);

        assertTrue(imports.getLibraries().contains("java.util.LinkedList"));
        assertEquals(imports.getLibraries().size(), 1);
    }

    @Test
    public void getLibrariesToImportTest5() {
        String input = "String[] words = {\"A\", \"B\", \"B\", \"D\", \"C\", \"A\"};\n" +
                "        System.out.println( \"original: \" + Arrays.toString(words));\n" +
                "        System.out.println( \"as a set: \" + Arrays.toString(makeSet(words)));\n" +
                "        \n" +
                "        Rectangle[] rectList = {new Rectangle(), new Rectangle(), \n" +
                "                    new Rectangle(0, 1, 2, 3), new Rectangle(0, 1, 2, 3)};\n" +
                "        System.out.println( \"original: \" + Arrays.toString(rectList));\n" +
                "        System.out.println( \"as a set: \" + Arrays.toString(makeSet(rectList)));     \n" +
                "        \n" +
                "        \n" +
                "        Object[] mixed = {\"A\", \"C\", \"A\", \"B\", new Rectangle(),\n" +
                "                new Rectangle(), \"A\", new Rectangle(0, 1, 2, 3), \"D\"};\n" +
                "        System.out.println( \"original: \" + Arrays.toString(mixed));\n" +
                "        System.out.println( \"as a set: \" + Arrays.toString(makeSet(mixed)));\n" +
                "assert data != null : \"Failed precondition makeSet. parameter cannot be null\";\n" +
                "        assert noNulls(data) : \"Failed precondition makeSet. no elements of parameter can be null\";\n" +
                "        Object[] result = new Object[data.length];\n" +
                "        int numUnique = 0;\n" +
                "        boolean found;\n" +
                "        int indexInResult;\n" +
                "        for(int i = 0; i < data.length; i++){\n" +
                "            // maybe should break this out into another method\n" +
                "            indexInResult = 0;\n" +
                "            found = false;\n" +
                "            while(!found && indexInResult < numUnique){\n" +
                "                found = data[i].equals(result[indexInResult]);\n" +
                "                indexInResult++;\n" +
                "            }\n" +
                "            if( ! found ){\n" +
                "                result[numUnique] = data[i];\n" +
                "                numUnique++;\n" +
                "            }\n" +
                "        }\n" +
                "        Object[] result2 = new Object[numUnique];\n" +
                "        System.arraycopy(result, 0, result2, 0, numUnique);\n" +
                "        return result2;";

        imports = importerService.getLibrariesToImport(imports, input);

        assertTrue(imports.getLibraries().contains("java.awt.Rectangle"));
        assertTrue(imports.getLibraries().contains("java.util.Arrays"));
        assertEquals(imports.getLibraries().size(), 2);
    }

    @Test
    public void getLibrariesToImportTest6() {
        String input = "java.util.Scanner scannerToReadAirlines = null;\n" +
                "        try{\n" +
                "            scannerToReadAirlines = new java.util.Scanner(new File(\"airlines.txt\"));\n" +
                "        }\n" +
                "        catch(IOException e){\n" +
                "            System.out.println(\"Could not connect to file airlines.txt.\");\n" +
                "            System.exit(0);\n" +
                "        }\n" +
                "        if(scannerToReadAirlines != null){\n" +
                "            ArrayList<Airline> airlinesPartnersNetwork = new ArrayList<Airline>();\n" +
                "            Airline newAirline;\n" +
                "            String lineFromFile;\n" +
                "            String[] airlineNames;\n" +
                "            \n" +
                "            while( scannerToReadAirlines.hasNext() ){\n" +
                "                lineFromFile = scannerToReadAirlines.nextLine();\n" +
                "                airlineNames = lineFromFile.split(\",\");\n" +
                "                newAirline = new Airline(airlineNames);\n" +
                "                airlinesPartnersNetwork.add( newAirline );\n" +
                "            }\n" +
                "            System.out.println(airlinesPartnersNetwork);\n" +
                "            System.out.print(\"Enter airline miles are on: \");\n" +
                "            String start = keyboard.nextLine();\n" +
                "            System.out.print(\"Enter goal airline: \");\n" +
                "            String goal = keyboard.nextLine();\n" +
                "            ArrayList<String> pathForMiles = new ArrayList<String>();\n" +
                "            ArrayList<String> airlinesVisited = new ArrayList<String>();\n" +
                "            if( canRedeem(start, goal, pathForMiles, airlinesVisited, airlinesPartnersNetwork))\n" +
                "                System.out.println(\"Path to redeem miles: \" + pathForMiles);\n" +
                "            else\n" +
                "                System.out.println(\"Cannot convert miles from \" + start + \" to \" + goal + \".\");\n" +
                "        }\n" +
                "ArrayList<String> pathForMiles, ArrayList<String> airlinesVisited,\n" +
                "            ArrayList<Airline> network){\n" +
                "        if(current.equals(goal)){\n" +
                "            //base case 1, I have found a path!\n" +
                "            pathForMiles.add(current);\n" +
                "            return true;\n" +
                "        }\n" +
                "        else if(airlinesVisited.contains(current))\n" +
                "            // base case 2, I have already been here\n" +
                "            // don't go into a cycle\n" +
                "            return false;\n" +
                "        else{\n" +
                "            // I have not been here and it isn't\n" +
                "            // the goal so check its partners\n" +
                "            // now I have been here\n" +
                "            airlinesVisited.add(current);\n" +
                "            \n" +
                "            // add this to the path\n" +
                "            pathForMiles.add(current);\n" +
                "            \n" +
                "            // find this airline in the network\n" +
                "            int pos = -1;\n" +
                "            int index = 0;\n" +
                "            while(pos == -1 && index < network.size()){\n" +
                "                if(network.get(index).getName().equals(current))\n" +
                "                    pos = index;\n" +
                "                index++;\n" +
                "            }\n" +
                "            //if not in the network, no partners\n" +
                "            if( pos == - 1)\n" +
                "                return false;\n" +
                "            \n" +
                "            // loop through partners\n" +
                "            index = 0;\n" +
                "            String[] partners = network.get(pos).getPartners();\n" +
                "            boolean foundPath = false;\n" +
                "            while( !foundPath && index < partners.length){\n" +
                "                foundPath = canRedeem(partners[index], goal, pathForMiles, airlinesVisited, network);\n" +
                "                index++;\n" +
                "            }\n" +
                "            if( !foundPath )\n" +
                "                pathForMiles.remove( pathForMiles.size() - 1);\n" +
                "            return foundPath;\n" +
                "        }";

        imports = importerService.getLibrariesToImport(imports, input);

        assertTrue(imports.getLibraries().contains("java.util.ArrayList"));
        assertTrue(imports.getLibraries().contains("java.util.Scanner"));
        assertTrue(imports.getLibraries().contains("java.io.IOException"));
        assertTrue(imports.getLibraries().contains("java.io.File"));
        assertEquals(imports.getLibraries().size(), 4);
    }
}
