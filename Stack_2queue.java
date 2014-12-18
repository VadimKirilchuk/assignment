import java.util.*;

public class Stack_2queue
{
public static void main(String[] args){

MStack<String> stack= new MStack<>(3);

stack.push("1");
stack.push("2");
stack.push("3");
System.out.println(stack);
stack.pop();
stack.pop();
stack.pop();
stack.pop();

System.out.println(stack);

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
queueFirst.add(elem);
}
}

public E pop(){

if(queueFirst.isEmpty()){
System.out.println("stack is Empty");
return null;}
else{
while(queueFirst.size()!=1){
queueSecond.add(queueFirst.remove());
}

E obj=queueFirst.remove();

while(!queueSecond.isEmpty()){
queueFirst.add(queueSecond.remove());
}

return obj;
}

}
public boolean isEmpty(){

return queueFirst.isEmpty();
}

}


