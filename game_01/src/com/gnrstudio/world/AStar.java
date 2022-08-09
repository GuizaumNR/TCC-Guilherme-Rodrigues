package com.gnrstudio.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class AStar {

	public static double LastTime = System.currentTimeMillis();
	private static Comparator<Node> nodeSorter = new Comparator<Node>() {
		
		@Override	
		public int compare(Node n0, Node n1) {
			if(n1.fCost < n0.fCost) {
				return +1;
			}
			if(n1.fCost > n0.fCost) {
				return -1;
			}
			return 0;
		}
	};
	
	public static boolean clear() {
		if(System.currentTimeMillis() - LastTime >= 1000) {
			return true;
		}
		return false;
	}
	
	public static List<Node> findPath(World world, Vector2i start, Vector2i end){
		LastTime = System.currentTimeMillis();
		List<Node> openList = new ArrayList<Node>(); //posições possiveis de percorrer
		List<Node> closedList = new ArrayList<Node>();// posições invalidas
		
		Node current = new Node(start, null, 0, getDistance(start, end));
		openList.add(current);
		while(openList.size() > 0) {// chance de encontrar o caminho
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if(current.tile.equals(end)) {
				//chegou no ponto final, agora só retornar o valor
				List<Node> path = new ArrayList<Node>();
				while(current.parent != null) {
					path.add(current);
					current = current.parent; 
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			
			openList.remove(current);//se n é o caminho certo
			closedList.add(current);//vai pra lista dos caminhos errados
			
			for(int i = 0; i < 9; i++) {
				if(i == 4) continue;//posição que ja esta
				int x = current.tile.x;
				int y = current.tile.y;
				int xi = (i%3) - 1;
				int yi = (i/3) - 1;
				
				Tile tile = World.tiles[x + xi + ((y + yi) * World.WIDTH)];
				if(tile == null) continue;
				if(tile instanceof WallTile) continue;
				if(i == 0) { //para andar na diagonal
					Tile test = World.tiles[x+xi+1+((y+yi) * World.WIDTH)];
					Tile test2 = World.tiles[x+xi+((y+yi+1) * World.WIDTH)];
					if(test instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				}else if(i == 2) {
					Tile test = World.tiles[x+xi+1+((y+yi) * World.WIDTH)];
					Tile test2 = World.tiles[x+xi+((y+yi+1) * World.WIDTH)];
					if(test instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				}else if(i == 6) {
					Tile test = World.tiles[x+xi+((y+yi - 1) * World.WIDTH)];
					Tile test2 = World.tiles[x+xi+1+((y+yi) * World.WIDTH)];
					if(test instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				}else if(i == 8) {
					Tile test = World.tiles[x+xi+((y+yi-1) * World.WIDTH)];
					Tile test2 = World.tiles[x+xi-1+((y+yi) * World.WIDTH)];
					if(test instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				}
				
				Vector2i a = new Vector2i(x+xi,y+yi);//posição possivel
				double gCost = current.gCost + getDistance(current.tile,a);//a "demora" de ir de um tile pra outro
				double hCost = getDistance(a,end);//distancia atual e o ponto onde chegar
				
				Node node = new Node(a, current, gCost, hCost);
				
				if(vecInList(closedList,a) && gCost >= current.gCost) continue;
				
				if(!vecInList(openList,a)) {
					openList.add(node);
				}else if(gCost < current.gCost) {//vendo se esse caminho é menor do q o q já ta na lista
					openList.remove(current);
					openList.add(node);
				}
			}
		}
		closedList.clear();
		return null;
	}
	
	private static boolean vecInList(List<Node> list, Vector2i vector) {
		 for(int i = 0; i < list.size(); i++) {
			 if(list.get(i).tile.equals(vector)) {
				 return true;
			 }
		 }
		 
		 return false;
	}


	private static double getDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.x - goal.x;
		double dy = tile.y - goal.y;
		
		return Math.sqrt(dx*dx + dy*dy);
	}
}
