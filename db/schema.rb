# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20161126191153) do

  create_table "products", force: :cascade do |t|
    t.string   "ean"
    t.string   "prod_attributes"
    t.string   "resource_type"
    t.string   "label_name"
    t.string   "marketing_name"
    t.string   "description"
    t.string   "category"
    t.string   "subcategory"
    t.string   "pricing_unit"
    t.string   "picture_urls"
    t.datetime "created_at",          null: false
    t.datetime "updated_at",          null: false
    t.string   "ean_replacing"
    t.string   "ean_replaced"
    t.string   "brand"
    t.string   "ingredient_defaults"
    t.string   "ingredient_type"
  end

  create_table "recipes", force: :cascade do |t|
    t.integer  "code"
    t.string   "name"
    t.string   "portions"
    t.string   "preparation_time"
    t.string   "categories"
    t.string   "pictures"
    t.string   "picture_urls"
    t.string   "energy_amounts"
    t.string   "ingredients"
    t.string   "instructions"
    t.string   "end_note"
    t.string   "description"
    t.datetime "date_created"
    t.datetime "date_modified"
    t.string   "stamp"
    t.string   "recipe_use"
    t.string   "resource_type"
    t.string   "url"
    t.string   "url_slug"
    t.string   "sort"
    t.string   "lat"
    t.string   "long"
    t.string   "special_diets"
    t.string   "piece_size"
    t.string   "video_url"
    t.string   "video_embed_url"
    t.string   "trend_words"
    t.datetime "created_at",       null: false
    t.datetime "updated_at",       null: false
  end

end
