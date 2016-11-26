class Product < ApplicationRecord
  serialize :prod_attributes, JSON
end
