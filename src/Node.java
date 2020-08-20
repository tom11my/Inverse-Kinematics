import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * would be useful if we had a traverse function that took in a function itself, 
 * so we could apply this latter function to each element in tree (essentially a for each)
 */
 public class Node<T> {
        private T data;
        //
        private Node<T> parent;
        private ArrayList<Node<T>> children;
        //for creating root node (tree)
        public Node(T data) {
        	this.data = data;
        	this.setChildren(new ArrayList<Node<T>>());
        }
        public Node(T data, ArrayList<Node<T>> children) {
        	this.data = data;
        	this.children = children;
        	this.setChildren(new ArrayList<Node<T>>());

        }
        public Node(T data, Node<T> parent) {
        	this.data= data;
        	this.parent = parent;        	
        	this.setChildren(new ArrayList<Node<T>>());
        }
        public Node(T data, Node<T> parent, ArrayList<Node<T>> children) {
        	this.setData(data);
        	this.setParent(parent);
        	this.setChildren(children);
        }
        public void addChild(Node<T> child) {
        	children.add(child);
        }
		public T getData() {
			return data;
		}
		public void setData(T data) {
			this.data = data;
		}
		public Node<T> getParent() {
			return parent;
		}
		public void setParent(Node<T> parent) {
			this.parent = parent;
		}
		public List<Node<T>> getChildren() {
			return children;
		}
		public void setChildren(ArrayList<Node<T>> children) {
			this.children = children;
		}
		public String toString () {
			return  "Data: " + data;
		}
		
		public void traverse() {
			this.getChildren().forEach(child -> child.traverse());
			System.out.println(this);
		}
		//NOTE: this version of traverseAdd only adds to the children and not to the root node
		//this is to ensure our placeholder node for action tree events remains tokenless (or negative)
		public void traverseAdd(int tokens) {
			/*this.getChildren().forEach(child -> child.traverseAdd(tokens));
			((Action)this.getData()).addTokens(tokens);*/
			this.getChildren().forEach(child -> {
				((Action)child.getData()).addTokens(tokens);
				child.traverseAdd(tokens);
			});
		}
		//draws joints of body with transformation applying outward to children
		public void traverseDraw(Graphics2D g2d, Mat33 multParents, Vec3 parentLoc) {
			Mat33 relParent = parent == null ? new Mat33():((MovingBodyPart)parent.getData()).getTransform();
			multParents = multParents.multiply(relParent);
			//prevents a parentLoc from being multiplied more than once
			Vec3 curParentLoc = parentLoc.multiply(multParents);
			((MovingBodyPart)this.getData()).draw(g2d, curParentLoc.trim());
			//draws "connectors" or "bones"
			if(parent != null) {
				Vec2 realCurLoc = curParentLoc.multiply(((MovingBodyPart)this.getData()).getTransform()).trim();
				g2d.drawLine((int)curParentLoc.getX(), (int)curParentLoc.getY(), (int)realCurLoc.getX(), (int)realCurLoc.getY());
			}
			for(Node<T> child: this.getChildren()) {
				child.traverseDraw(g2d, multParents, parentLoc);
			}
		}
		//draws joints of body with transformation applying inward to parents
		public void traverseDrawIn(Graphics2D g2d, Mat33 multChildren, Vec3 parentLoc) {
			Mat33 relParent = parent == null ? new Mat33():((MovingBodyPart)parent.getData()).getTransform();
			for(Node<T> child: this.getChildren()) {
				child.traverseDrawIn(g2d, multChildren, parentLoc);
			}
			multChildren = multChildren.multiply(relParent);
			//draws "connectors" or "bones"
			
		}
		//asynchronous movement for finding the first tokenful actions as we traverse down an action tree
		//should have a single parameter that is initially an empty arrayList
		//Condition: we must ignore "this" node, so each action tree must first start with a placeholder node
		public ArrayList<Action> findLegalActions(ArrayList<Action> legalActions) {
			for(Node<T> child: this.getChildren()) {
				if(((Action)child.getData()).hasTokens()) {
					legalActions.add((Action)child.getData());
				} else child.findLegalActions(legalActions);
				
			}
			//assume that the elements of legalActions update after recursive calls
			return legalActions;
		}
		public Node<T> find (Node<T> node) {
			for(Node<T> temp: children) {
				if(temp == node)
					return temp;
				if(temp.find(node) != null)
					return temp.find(node);
			}
			return null;
		}

}