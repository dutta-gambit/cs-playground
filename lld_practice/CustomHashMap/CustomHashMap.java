package cs.CustomHashMap;

//intrusive implementation

public class CustomHashMap {

  public int capacityOfMap = 32;

  public int numberOfElementInTheMap;

  public Double loadFactor = 0.75;

  public Integer growthFactor = 2;

  Node[] nodes = new Node[capacityOfMap];



  public void put(Integer key, Integer data){
    Boolean valueUpdated = false;
    Integer bucketIndex = getIndexOf(key);
    Node curr = nodes[bucketIndex];
    Integer countOfElementInBucket = 0;
    if(curr == null){
      Node newNode = new Node(key, data);
      nodes[bucketIndex] = newNode;
      countOfElementInBucket++;
      numberOfElementInTheMap++;
    }
    else{
      Node lastNode = null;
      while(curr != null) {
        countOfElementInBucket++;
        if (curr.key.equals(key)) {
          curr.value = data;
          valueUpdated = true;
        }
        if(curr.next == null){
          lastNode = curr;
        }
        curr = curr.next;
      }

      if(!valueUpdated){
        Node newNode = new Node(key, data);
        lastNode.next = newNode;
        numberOfElementInTheMap++;
        countOfElementInBucket++;
      }

    }
    //loadFactorCheck
    boolean isViolatingLoadFactor =  (double)  numberOfElementInTheMap/capacityOfMap > 0.75;

  }

  public Integer get(Integer key){
    Integer bucketIndex = getIndexOf(key);
    Node curr = nodes[bucketIndex];
    if(curr == null){
      return  null;
    }
    while(curr != null){
      if (curr.key.equals(key)) {
        return curr.value;
      }
      curr = curr.next;
    }
    return null;
  }

  public void remove(Integer key){

    Integer bucketIndex = getIndexOf(key);
    Node curr = nodes[bucketIndex];

    if(curr == null){
      System.out.println("No Such Key Found");
      return;
    }

    Node prev = null;
    while(curr != null){

      if(curr.key.equals(key)){

        if(prev == null){ //first element in the bucket
           nodes[bucketIndex] = curr.next;
          System.out.println("Key Removed");
          numberOfElementInTheMap--;
          return;
        }
        else{
          prev.next = curr.next; // skip curr and gc will pick it up
          System.out.println("Key Removed");
          numberOfElementInTheMap--;
          return;
        }

      }
      prev = curr;
      curr = curr.next;
    }
  }

  private Integer getIndexOf(Integer key){
    Integer hashcode = key.hashCode();
    Integer bucketIndex = (hashcode & (capacityOfMap - 1));
    return bucketIndex;
  }


  public void resize(){
    capacityOfMap = capacityOfMap * growthFactor;
    Node[] resizeNode = new Node[capacityOfMap];
    numberOfElementInTheMap = 0;
    for(int i = 0 ; i < nodes.length; i++){
      Node currInOldNode = nodes[i];
      Integer newIndex = getIndexOf(currInOldNode.key);
      Boolean valueUpdated = false;
      Node currInResizeNode = resizeNode[newIndex];
      if(currInResizeNode == null){
        Node newNode = new Node(currInOldNode.key, currInOldNode.value);
        resizeNode[newIndex] = newNode;
        numberOfElementInTheMap++;
      }
      else{
        Node lastNode = null;
        while( currInOldNode != null) {

          if (currInResizeNode.key.equals(currInOldNode.key)) {
            currInResizeNode.value = currInOldNode.value;
            valueUpdated = true;
          }
          if(currInResizeNode.next == null){
            lastNode = currInResizeNode;
          }
          currInOldNode = currInOldNode.next;
        }

        if(!valueUpdated){
          Node newNode = new Node(currInOldNode.key, currInOldNode.value);
          lastNode.next = newNode;
          numberOfElementInTheMap++;
        }

      }


    }
    nodes = resizeNode;
  }

}
