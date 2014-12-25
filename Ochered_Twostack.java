import java.util.*;
import java.util.Stack;
public class Ochered_Twostack
{
public static void main(String[] args)
{

Que<String> que=new Que<>(3);
que.push("1");

que.push("2");
que.push("3");
que.pop();
que.push("5");
System.out.println(que.pop());

}






}
class Que<E>
{
private Stack<E> stack1=new Stack<>();
private Stack<E> stack2=new Stack<>();
private int capacity;

public Que(int capacity){
this.capacity=capacity;
}
public String toString(){

return "stack2 "+stack2.toString()+"stack1 "+stack1.toString();

}
public void push(E num)
{

if(stack1.size()+stack2.size()==capacity)
System.out.println("Queue is full");
else
{
stack1.push(num);


}


}


public E pop()
{
if (stack1.isEmpty()&&stack2.isEmpty()){
System.out.println("Stack is Empty");
return null;
}
else
if(stack2.isEmpty()){

while(!stack1.isEmpty()){

stack2.push(stack1.pop());
}

return stack2.pop();
}
else{
return stack2.pop();
}
}


}


