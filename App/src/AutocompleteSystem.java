import java.util.*;

class TrieNode {

    Map<Character, TrieNode> children = new HashMap<>();
    List<String> queries = new ArrayList<>();
}

public class AutocompleteSystem {

    private TrieNode root = new TrieNode();

    private HashMap<String,Integer> frequencyMap = new HashMap<>();

    // insert query into trie
    public void insert(String query){

        TrieNode node = root;

        for(char c : query.toCharArray()){

            node.children.putIfAbsent(c,new TrieNode());
            node = node.children.get(c);

            node.queries.add(query);
        }
    }

    // update frequency
    public void updateFrequency(String query){

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query,0)+1);

        insert(query);
    }

    // search suggestions
    public List<String> search(String prefix){

        TrieNode node = root;

        for(char c : prefix.toCharArray()){

            if(!node.children.containsKey(c))
                return new ArrayList<>();

            node = node.children.get(c);
        }

        List<String> results = node.queries;

        // sort by frequency
        results.sort((a,b)->
                frequencyMap.getOrDefault(b,0) -
                        frequencyMap.getOrDefault(a,0));

        return results.subList(0, Math.min(10,results.size()));
    }

    public static void main(String[] args){

        AutocompleteSystem ac = new AutocompleteSystem();

        ac.updateFrequency("java tutorial");
        ac.updateFrequency("javascript");
        ac.updateFrequency("java download");

        System.out.println(ac.search("jav"));
    }
}