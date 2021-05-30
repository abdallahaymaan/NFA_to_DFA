import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



class state {
	int [] name;
	int [] next0;
	int [] next1;
	Boolean goal;
//	int [] eClosure;
	public state(int[] name, int[] next0 , int[] next1,Boolean goal) {
		this.name = name;
		this.next0 = next0;
		this.next1 = next1;
		this.goal=goal;	
//		this.eClosure = eClosure;
	}
}

public class nfa {

	//0,0;1,2;3,3#0,0;0,1;2,3;3,3#1,2#3
	
	int numberOfStates;
	int[][] eclosure;
	List<int[]> states;
	List<state> dfa_list;

	
	public nfa(String s) {
		this.numberOfStates = statesAmount(s);
		
		String[] first_split= s.split("#");
		String [] eTransitions = first_split[2].split(";");
		this.dfa_list=new ArrayList<state>();
		this.states=new ArrayList<int[]>();


		//eclosure
		this.eclosure= new int [numberOfStates][];
//		System.out.println(numberOfStates);

		for(int i = 0 ; i < numberOfStates ; i++) {
			int [] stateClosure = null ;
			stateClosure= addX(0, stateClosure, i);
			
			for(int j = 0 ; j < eTransitions.length ; j++) {
				String element = eTransitions[j];
				if(check(stateClosure,  Character.getNumericValue(element.charAt(0)))) {
					if(!check(stateClosure,  Character.getNumericValue(element.charAt(element.length()-1)))) {
						stateClosure= addX(stateClosure.length, stateClosure,  Character.getNumericValue(element.charAt(element.length()-1)));
						j = 0;
					}
				}

			}
			eclosure[i] = stateClosure;	
		}
		

		//states
		String [] zeroTransitions = first_split[0].split(";");
		String [] oneTransitions = first_split[1].split(";");
		String [] goalStates = first_split[3].split(","); 
		Arrays.sort(eclosure[0]);
		states.add(eclosure[0]);
		
		for(int i = 0 ; i<this.states.size() ; i++) {
			
			int [] newStateName = states.get(i);
			int [] newStateZero = {} ;
			for(String transition:zeroTransitions) {

				if(check(newStateName,  Character.getNumericValue(transition.charAt(0)))) {
					if(!check(newStateZero,  Character.getNumericValue(transition.charAt(transition.length()-1)))) {
						newStateZero = addX(newStateZero.length, eclosure[ Character.getNumericValue(transition.charAt(transition.length()-1))], newStateZero);
					}
				}
			}

			Arrays.sort(newStateZero);
			
			if(!newArrayInStates(newStateZero, this.states)) {
				this.states.add(newStateZero);
			}
			
			
			int [] newStateOne = {} ;
			for(String transition:oneTransitions) {
				if(check(newStateName, Character.getNumericValue(transition.charAt(0)))) {
					if(!check(newStateOne,  Character.getNumericValue(transition.charAt(transition.length()-1)))) {
						newStateOne = addX(newStateOne.length, eclosure[ Character.getNumericValue(transition.charAt(transition.length()-1))], newStateOne);
					}
				}
			}
			Arrays.sort(newStateOne);
			if(!newArrayInStates(newStateOne, this.states)) {
				this.states.add(newStateOne);
			}
			
			Boolean newStateGoal = checkGoalState(goalStates, newStateName);
			
			
			state x = new state(newStateName, newStateZero, newStateOne, newStateGoal);
			dfa_list.add(x);
			
			
		}
	}
	
	public Boolean run(String s) {
		state curr_state= dfa_list.get(0);
		for (int i = 0; i < s.length(); i++){
		    int c = Character.getNumericValue(s.charAt(i));
		    int[] new_state;
		    if(c==0) {
		    	new_state = curr_state.next0;
		    }
		    else {
		    	new_state = curr_state.next1;
		    }
		    
		    for(int j = 0 ; j < dfa_list.size();j++) {
		    	if(Arrays.equals(dfa_list.get(j).name, new_state)) {
		    		curr_state = dfa_list.get(j);
		    	}
		    }
		    
//		    int new_state_index ;
//		    if(c==0) {
//		    	new_state_index = curr_state.next0;
//		    }
//		    else {
//		    	new_state_index = curr_state.next1;
//
//		    }
//		    curr_state = dfa_list.get(new_state_index);
//		    
//		
		}
		
		System.out.println(curr_state.goal);
		return curr_state.goal;
		
	}

	
	public Boolean newArrayInStates(int [] newArray,List<int[]> allStates ) {
		
		for(int i = 0 ; i<allStates.size();i++) {
			if(Arrays.equals(newArray , allStates.get(i)))
				return true;
		}
		
		return false;
		
	}
	
	 public static int[] addX(int n, int arr[], int x)
	    {
	        int i;
	        int newarr[] = new int[n + 1];
	        for (i = 0; i < n; i++)
	            newarr[i] = arr[i];
	  
	        newarr[n] = x;
	        return newarr;
	    }
	 
	 public Boolean checkGoalState(String[] goalStates , int[]states) {
		
		 for(int i = 0; i < states.length ; i++) {
			 
			 for(int j =0 ; j<goalStates.length ; j++) {
				 if(states[i] == Integer.parseInt(goalStates[j])) {
					 return true;
				 }
			 }
		 }
		 
		 
		 
		 return false;
	 }
	 
	 public static int[] addX(int n, int addarr[], int [] toarr)
	    {
	        int i;
	  
	        // create a new array of size n+1
	        int newarr[] = new int[n + addarr.length];
	  
	        // insert the elements from
	        // the old array into the new array
	        // insert all elements till n
	        // then insert x at n+1
	        for (i = 0; i < n; i++)
	            newarr[i] = toarr[i];
	  
	        for(i = n ; i < newarr.length ; i++) {
	        	newarr[i]= addarr[i-n];
	        }
	        removeDuplicateElements(newarr, newarr.length);
	  
	        return newarr;
	    }

//	 1,2;4,5;6,7;8,9#2,3;5,6#0,1;0,4;3,1;3,4;7,8;7,10;9,8;9,10#10
	 
//	 aa[i]= 2,3;5,6
//	 aaa[i] = 2,3
//	 aaaa[i] = 2
	public int  statesAmount(String s) {
		
		int max = 0 ;
		String [] aa = s.split("#");
			
		for(int i = 0 ; i < aa.length ; i++) {
			String [] aaa = aa[i].split(";");
			for(int j = 0 ; j < aaa.length ; j++) {
				String [] aaaa = aaa[j].split(",");
				for(int z = 0 ; z < aaaa.length ; z++) {
					if(Integer.parseInt(aaaa[z]) > max) {
						max = Integer.parseInt(aaaa[z]);
					}
				}
			}
		}
		
//		for(Character a:s.toCharArray()) {
//			if(Character.isDigit(a)) {
//				if(Character.valueOf(a) > max ) {
//					max =Integer.parseInt(String.valueOf(a)); 
//				}
//			}
//		}
		System.out.print(max + 1);
		return max+1;
	}
	private static boolean check(String [] arr , String valueToCheck) {
		for (String element : arr) {
			
            if (Integer.parseInt(element)== Integer.parseInt(valueToCheck)) {
                return true;
            }
        }
		return false;
	}
	private static boolean check(int [] arr , int valueToCheck) {
		for (int element : arr) {
			
            if (element== valueToCheck) {
                return true;
            }
        }
		return false;
	}
	public static int removeDuplicateElements(int arr[], int n){  
        if (n==0 || n==1){  
            return n;  
        }  
        int[] temp = new int[n];  
        int j = 0;  
        for (int i=0; i<n-1; i++){  
            if (arr[i] != arr[i+1]){  
                temp[j++] = arr[i];  
            }  
         }  
        temp[j++] = arr[n-1];     
        // Changing original array  
        for (int i=0; i<j; i++){  
            arr[i] = temp[i];  
        }  
        return j;  
    }  
	
	
	
	public static void main (String[]args) {
		nfa nfa1 = new nfa("1,2;4,5;6,7;8,9#2,3;5,6#0,1;0,4;3,1;3,4;7,8;7,10;9,8;9,10#10");
		nfa1.run("0100");
		nfa1.run("01010");
		nfa1.run("01");
		nfa1.run("0101");
		nfa1.run("01001"); 
		
		nfa nfa2 = new nfa("4,5;6,7#0,1;2,3#1,2;1,4;3,2;3,4;5,6;5,8;7,6;7,8#8");
		nfa2.run("1");
		nfa2.run("1001");
		nfa2.run("111");
		nfa2.run("100");
		nfa2.run("110");
		
	}
}

