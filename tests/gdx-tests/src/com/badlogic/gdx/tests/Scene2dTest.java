/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.tests;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.tests.utils.GdxTest;

public class Scene2dTest extends GdxTest {
	Stage stage;
	private FloatAction meow = new FloatAction(10, 5);
	private TiledDrawable patch;

	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		final TextureRegion region = new TextureRegion(new Texture("data/badlogic.jpg"));
		final Actor actor = new Actor() {
			public void draw (Batch batch, float parentAlpha) {
				Color color = getColor();
				batch.setColor(color.r, color.g, color.b, parentAlpha);
				batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation());
			}
		};
		actor.setBounds(15, 15, 100, 100);
		actor.setOrigin(50, 50);
		stage.addActor(actor);
		actor.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("up " + event.getTarget());
			}
		});

		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		VerticalGroup g = new VerticalGroup();
		g.setPosition(10, 100);
		g.setReverse(true);
		g.setSpacing(5);
		stage.addActor(g);
		for (int i = 0; i < 10; i++) {
			g.addActor(new TextButton("button " + i, skin));
		}
		g.pack();

		HorizontalGroup h = new HorizontalGroup();
		h.setPosition(100, 100);
		h.setReverse(true);
		h.setSpacing(5);
		stage.addActor(h);
		for (int i = 0; i < 7; i++) {
			h.addActor(new TextButton("button " + i, skin));
		}
		h.pack();

		final TextButton button = new TextButton("Fancy Background", skin);

// button.addListener(new ClickListener() {
// public void clicked (InputEvent event, float x, float y) {
// System.out.println("click! " + x + " " + y);
// }
// });

		button.addListener(new ActorGestureListener() {
			public boolean longPress (Actor actor, float x, float y) {
				System.out.println("long press " + x + ", " + y);
				return true;
			}

			public void fling (InputEvent event, float velocityX, float velocityY, int button) {
				System.out.println("fling " + velocityX + ", " + velocityY);
			}

			public void zoom (InputEvent event, float initialDistance, float distance) {
				System.out.println("zoom " + initialDistance + ", " + distance);
			}

			public void pan (InputEvent event, float x, float y, float deltaX, float deltaY) {
				event.getListenerActor().moveBy(deltaX, deltaY);
				if (deltaX < 0) System.out.println("panning " + deltaX + ", " + deltaY + " " + event.getTarget());
			}
		});

// button.addListener(new ChangeListener() {
// public void changed (ChangeEvent event, Actor actor) {
// // event.cancel();
// }
// });

		button.setPosition(50, 50);
		stage.addActor(button);

// List select = new List(skin);
// select.setBounds(200, 200, 100, 100);
// select.setItems(new Object[] {1, 2, 3, 4, 5});
// stage.addActor(select);

// stage.addListener(new ChangeListener() {
// public void changed (ChangeEvent event, Actor actor) {
// System.out.println(actor);
// }
// });

		meow.setDuration(2);

		actor.addAction(forever(sequence(moveBy(50, 0, 2), moveBy(-50, 0, 2), run(new Runnable() {
			public void run () {
				actor.setZIndex(0);
			}
		}))));
		// actor.addAction(parallel(rotateBy(90, 2), rotateBy(90, 2)));
		// actor.addAction(parallel(moveTo(250, 250, 2, elasticOut), color(RED, 6), delay(0.5f), rotateTo(180, 5, swing)));
		// actor.addAction(forever(sequence(scaleTo(2, 2, 0.5f), scaleTo(1, 1, 0.5f), delay(0.5f))));

		patch = new TiledDrawable(skin.getRegion("default-round"));

		Window window = new Window("Moo", skin);
		Label lbl = new Label("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJ", skin);
		lbl.setWrap(true);
		window.row();
		window.add(lbl).width(400);
		window.pack();
		window.pack();
		stage.addActor(window);
	}

	public void render () {
		// System.out.println(meow.getValue());
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		Table.drawDebug(stage);

		stage.getSpriteBatch().begin();
		patch.draw(stage.getSpriteBatch(), 300, 100, 126, 126);
		stage.getSpriteBatch().end();
	}

	public void resize (int width, int height) {
		stage.setViewport(width, height, true);
	}

	public boolean needsGL20 () {
		return true;
	}

	public void dispose () {
		stage.dispose();
	}
}
