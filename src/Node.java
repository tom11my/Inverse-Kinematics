import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/*
 * Serves as the template for Body and any Action Trees (future)
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
		public void traverseApply(Consumer<Link> func) {
			func.accept((Link) this.data);
			for (Node child : this.children) {
				child.traverseApply(func);
			}
		}
		public void traverseDraw(Graphics2D g2d, Vec2 parentLoc) {
			Link parent = (Link) this.data;
        	parent.draw(g2d, parentLoc);
			parentLoc = parent.head().loc(parentLoc);
			for (Node l : this.children) {
        		l.traverseDraw(g2d, parentLoc);
        		Link child = (Link) l.data;
        		Vec2 childLoc = child.head().loc(parentLoc);
				g2d.drawLine((int)parentLoc.getX(), (int)parentLoc.getY(),
						(int)childLoc.getX(),
						(int)childLoc.getY());
			}
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
			for(Node child: this.getChildren()) {
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
		/** Really need to create my own body class that extends this one. */
		public Vec2 findParentLinkLoc(Link link, Vec2 parent) {
			if (this.data == link) {
				return parent;
			}
			for (Node child : this.children) {
				Vec2 curLoc = ((Link)this.data).head().loc(parent);
				Vec2 found = child.findParentLinkLoc(link, curLoc);
				if (found != null) {
					return found;
				}
			}
			return null;
		}

}