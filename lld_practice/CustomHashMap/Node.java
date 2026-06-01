package cs.CustomHashMap;

public class Node {

  public Integer key;
  public Integer value;
  Node next;

  Node(Integer key, Integer value){
    this.key = key;
    this.value = value;
    this.next = null;
  }

  Node(Integer key, Integer value, Node next){
    this.key = key;
    this.value = value;
    this.next = next;
  }



}
