class CreateRecipes < ActiveRecord::Migration[5.0]
  def change
    create_table :recipes do |t|
      t.integer :code
      t.string :name
      t.string :portions
      t.string :preparation_time
      t.string :categories
      t.string :pictures
      t.string :picture_urls
      t.string :energy_amounts
      t.string :ingredients
      t.string :instructions
      t.string :end_note
      t.string :description
      t.datetime :date_created
      t.datetime :date_modified
      t.string :stamp
      t.string :recipe_use
      t.string :resource_type
      t.string :url
      t.string :url_slug
      t.string :sort
      t.string :lat
      t.string :long
      t.string :special_diets
      t.string :piece_size
      t.string :video_url
      t.string :video_embed_url
      t.string :trend_words

      t.timestamps
    end
  end
end
