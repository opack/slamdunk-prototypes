package com.slamdunk.toolkit.graphics.tiled;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Renderer capable de dessiner des SpriteMapObjects
 */
public class OrthogonalTiledMapRendererWithSprites extends OrthogonalTiledMapRenderer {

	public OrthogonalTiledMapRendererWithSprites(TiledMap map, float unitScale) {
		super(map, unitScale);
	}
	
	@Override
    public void renderObject(MapObject object) {
        if(object instanceof SpriteMapObject) {
        	SpriteMapObject spriteMapObject = (SpriteMapObject) object;
        	spriteMapObject.getSprite().draw(spriteBatch);
        }
    }
// DBG	
//    private List<Sprite> sprites;
//    private String spriteLayerName;
//
//    /**
//     * 
//     * @param map
//     * @param spriteLayerName La couche après laquelle dessiner les sprites
//     */
//    public OrthogonalTiledMapRendererWithSprites(TiledMap map, float unitScale, String spriteLayerName) {
//        super(map, unitScale);
//        sprites = new ArrayList<Sprite>();
//        this.spriteLayerName = spriteLayerName;
//    }
//
//    public void addSprite(Sprite sprite){
//        sprites.add(sprite);
//    }
//    
//    public void removeSprite(Sprite sprite) {
//    	sprites.remove(sprite);
//    }
//
//    @Override
//    public void render() {
//        beginRender();
//
//        for (MapLayer layer : map.getLayers()) {
//            if (layer.isVisible()) {
//                if (layer instanceof TiledMapTileLayer) {
//                    renderTileLayer((TiledMapTileLayer)layer);
//                } else {
//                    for (MapObject object : layer.getObjects()) {
//                        renderObject(object);
//                    }
//                }
//            }
//            
//            // Dessin des sprites juste après (donc "sur") la couche demandée
//            if(spriteLayerName.equals(layer.getName())){
//                for(Sprite sprite : sprites) {
//                    sprite.draw(getSpriteBatch());
//                }
//            }
//        }
//        
//        endRender();
//    }
}
