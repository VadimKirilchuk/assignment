import java.util.*;

public class Stack_2queue2
{
public static void main(String[] args){

MStack<String> stack= new MStack<>(3);

stack.push("1");
stack.push("2");
stack.push("3");
System.out.println(stack);




System.out.println(stack.pop()
);

}


}





class MStack<E>
{
private int capacity;

private LinkedList<E> queueFirst=new LinkedList<>();
private LinkedList<E> queueSecond=new LinkedList<>();



public MStack(int capacity){
this.capacity=capacity;


}
public String toString(){

return queueFirst.toString();
}
public void push(E elem){

if(queueFirst.size()==capacity){
System.out.println("stack is full");}
else{
if(queueFirst.isEmpty()){
queueFirst.add(elem);
}
else{
while(!queueFirst.isEmpty()){

queueSecond.add(queueFirst.remove());
}
queueFirst.add(elem);
while(!queueSecond.isEmpty()){
queueFirst.add(queueSecond.remove());
}



}


}
}

public E pop(){

if(queueFirst.isEmpty()){
System.out.println("stack is Empty");
return null;}
else{

return queueFirst.remove();
}

}
public boolean isEmpty(){

return queueFirst.isEmpty();
}

}


